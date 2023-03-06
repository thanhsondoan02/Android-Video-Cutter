package ai.ftech.base.common.navigation

interface IScreenAnim {
    fun enter(): Int
    fun exit(): Int
    fun popEnter(): Int
    fun popExit(): Int
}
