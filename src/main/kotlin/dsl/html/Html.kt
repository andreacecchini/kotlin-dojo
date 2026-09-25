package dsl.html

@DslMarker
annotation class HtmlMarker

@HtmlMarker
interface Tag {
    val name: String
    val children: MutableList<Tag>
    fun renderInner(): String = children.joinToString("") { it.render() }
    fun render(): String = "<$name>${renderInner()}</$name>"
}

abstract class AbstractTag : Tag {
    override val children: MutableList<Tag> = mutableListOf()
    protected fun <T : Tag> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }
}

abstract class TagWithContent : AbstractTag() {
    var content: String? = null
    override fun renderInner(): String = (content ?: "") + super.renderInner()
    operator fun String.unaryMinus() {
        content = this
    }
}

class Html : AbstractTag() {
    override val name: String
        get() = "html"
    fun head(init: Head.() -> Unit): Tag = initTag(Head(), init)
    fun body(init: Body.() -> Unit): Tag = initTag(Body(), init)
}

class Head : AbstractTag() {
    override val name: String
        get() = "head"
    fun title(init: Title.() -> Unit): Tag = initTag(Title(), init)
}

class Body : AbstractTag() {
    override val name: String
        get() = "body"
    fun p(init: Paragraph.() -> Unit): Tag = initTag(Paragraph(), init)
}

class Title : TagWithContent() {
    override val name: String
        get() = "title"
}

class Paragraph : TagWithContent() {
    override val name: String
        get() = "p"
}

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
