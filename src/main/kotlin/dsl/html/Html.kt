package dsl.html

@DslMarker
annotation class HtmlMarker

@HtmlMarker
abstract class Tag(val name: String) {
    val children: MutableList<Tag> = mutableListOf()
    fun render(): String = "<$name>${renderInner()}</$name>"
    protected fun <T : Tag> initTag(tag: T, init: T.() -> Unit): T = tag.apply { init() }.also { children.add(it) }
    protected open fun renderInner(): String = children.joinToString("") { it.render() }
}

abstract class TagWithContent(name: String) : Tag(name) {
    var content: String? = null
    override fun renderInner(): String = (content ?: "") + super.renderInner()
    operator fun String.unaryMinus() {
        content = this
    }
}

class Html : Tag("html") {
    fun head(init: Head.() -> Unit): Tag = initTag(Head(), init)
    fun body(init: Body.() -> Unit): Tag = initTag(Body(), init)
}

class Head : Tag("head") {
    fun title(init: Title.() -> Unit): Tag = initTag(Title(), init)
}

class Body : Tag("body") {
    fun p(init: Paragraph.() -> Unit): Tag = initTag(Paragraph(), init)
}

class Title : TagWithContent("title")

class Paragraph : TagWithContent("p")

fun html(init: Html.() -> Unit): Tag = Html().apply { init() }

fun main() {
    html {
        head {
            title { -"Kotlin Dsl" }
        }
        body {
            p { -"This is a paragraph" }
        }
    }.render().also(::println)
}
