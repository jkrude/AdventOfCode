package common

import common.extensions.List2D
import common.extensions.Lists2D.columns

fun render(
    frames: Iterable<List2D<Char>>,
    flip: Boolean = false,
    advanceOnEnter: Boolean = false,
) {

    // Setup terminal: clear + hide cursor
    print("\u001B[2J")      // clear screen
    print("\u001B[?25l")    // hide cursor
    Runtime.getRuntime().addShutdownHook(Thread {
        print("\u001B[?25h") // show cursor on exit
        System.out.flush()
    })
    for (frame in frames) {
        // clear grid
        // build full frame and print at once (move cursor to home first)
        val outData = if (flip) frame.columns() else frame
        // prefix moves caret to (1,1) e.g. back to top left
        val frameString = outData.joinToString(prefix = "\u001B[H", separator = "\n") {
            it.joinToString("")
        }
        print(frameString)
        if (advanceOnEnter) {
            System.`in`.read()
        } else {
            Thread.sleep(60) // ~16 FPS -> tweak to your needs
        }
        System.out.flush()
    }
}
