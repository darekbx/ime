package em.i.views

class PhotoManipulator {

    enum class Action {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        ZOOM_IN,
        ZOOM_OUT
    }

    private val panStep = 100
    private val zoomStep = 0.5

    private var zoom = 1
    private var panX = 0
    private var panY = 0


}