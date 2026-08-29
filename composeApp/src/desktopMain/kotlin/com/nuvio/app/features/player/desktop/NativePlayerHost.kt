package com.nuvio.app.features.player.desktop

import java.awt.Canvas
import java.awt.Color
import java.awt.Cursor
import java.awt.Graphics
import java.awt.Point
import java.awt.Toolkit
import java.awt.Window
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener
import java.awt.image.BufferedImage
import javax.swing.SwingUtilities

internal class NativePlayerHost : Canvas() {
    var onPeerReady: (() -> Unit)? = null
    var onDisplayableChanged: ((Boolean) -> Unit)? = null
    var onFirstPaint: (() -> Unit)? = null
    var onFirstFullSizePaint: (() -> Unit)? = null
    var onCursorActivity: (() -> Unit)? = null

    /**
     * Fired when the top-level window this host lives in regains OS focus (e.g. the user
     * alt-tabs away mid-playback and switches back). Without this, keyboard shortcuts stay dead
     * until the user left-clicks inside the video: nothing else re-requests focus for the AWT
     * Canvas or (on Windows) the WebView2 controls overlay that actually owns the key events, so
     * they're left pointing at whatever had focus before the window was deactivated.
     */
    var onWindowFocusGained: (() -> Unit)? = null
    private var focusListenerWindow: Window? = null
    private val windowFocusListener = object : WindowFocusListener {
        override fun windowGainedFocus(event: WindowEvent) {
            onWindowFocusGained?.invoke()
        }

        override fun windowLostFocus(event: WindowEvent) = Unit
    }
    private var firstPaintNotified = false
    private var firstFullSizePaintNotified = false
    private var controlsVisible = true
    private var cursorVisible = true

    private companion object {
        val hiddenCursor: Cursor by lazy {
            val image = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
            Toolkit.getDefaultToolkit().createCustomCursor(image, Point(0, 0), "nuvio-hidden-cursor")
        }
    }

    init {
        background = Color.BLACK
        ignoreRepaint = false
        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(event: MouseEvent) {
                noteCursorActivity()
            }

            override fun mouseDragged(event: MouseEvent) {
                noteCursorActivity()
            }
        })
        // On Linux/XWayland a heavyweight Canvas embedded in a Compose SwingPanel is not
        // guaranteed an expose-driven paint() when it is first laid out, so the paint()-based
        // first-full-size-paint signal (which unlocks the native attach) can never fire and
        // playback silently never starts. componentResized fires reliably on layout, so use it
        // to drive the same signal. Linux-only to keep macOS/Windows behaviour byte-identical.
        if (DesktopHostOs.current == DesktopHostOs.LINUX) {
            addComponentListener(object : ComponentAdapter() {
                override fun componentResized(event: ComponentEvent) {
                    repaint()
                    notifyFirstPaints()
                }

                override fun componentShown(event: ComponentEvent) {
                    repaint()
                    notifyFirstPaints()
                }
            })
        }
    }

    private fun notifyFirstPaints() {
        if (!firstPaintNotified) {
            firstPaintNotified = true
            onFirstPaint?.invoke()
        }
        if (!firstFullSizePaintNotified && width > 1 && height > 1) {
            firstFullSizePaintNotified = true
            onFirstFullSizePaint?.invoke()
        }
    }

    fun setControlsVisible(visible: Boolean) {
        controlsVisible = visible
        setCursorVisible(visible)
    }

    fun noteCursorActivity() {
        onCursorActivity?.invoke()
    }

    fun resetCursorVisibility() {
        controlsVisible = true
        setCursorVisible(true)
    }

    private fun setCursorVisible(visible: Boolean) {
        if (cursorVisible == visible) return
        cursorVisible = visible
        cursor = if (visible) Cursor.getDefaultCursor() else hiddenCursor
    }

    override fun update(graphics: Graphics) {
        paint(graphics)
    }

    override fun paint(graphics: Graphics) {
        graphics.color = Color.BLACK
        graphics.fillRect(0, 0, width, height)
        notifyFirstPaints()
    }

    override fun addNotify() {
        super.addNotify()
        onDisplayableChanged?.invoke(true)
        repaint()
        onPeerReady?.invoke()
        // The window ancestor can change (or only just exist) once the peer is created, so
        // (re)attach the focus listener here rather than trying to do it at construction time.
        SwingUtilities.getWindowAncestor(this)?.let { window ->
            if (window !== focusListenerWindow) {
                focusListenerWindow?.removeWindowFocusListener(windowFocusListener)
                window.addWindowFocusListener(windowFocusListener)
                focusListenerWindow = window
            }
        }
    }

    override fun removeNotify() {
        onDisplayableChanged?.invoke(false)
        firstPaintNotified = false
        firstFullSizePaintNotified = false
        onPeerReady = null
        onFirstPaint = null
        onFirstFullSizePaint = null
        focusListenerWindow?.removeWindowFocusListener(windowFocusListener)
        focusListenerWindow = null
        resetCursorVisibility()
        super.removeNotify()
    }
}
