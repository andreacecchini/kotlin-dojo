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

abstract class TagWithContent : Tag {
    var content: String? = null
    override fun renderInner(): String = (content ?: "") + super.renderInner()
    operator fun String.unaryMinus() {
        content = this
    }
}

class Html : Tag {
    override val name: String
        get() = "html"
    override val children: MutableList<Tag> = mutableListOf()

    fun head(init: Head.() -> Unit): Tag = Head().apply { init() }.also { children.add(it) }

    fun body(init: Body.() -> Unit): Tag = Body().apply { init() }.also { children.add(it) }
}

class Head : Tag {
    override val name: String
        get() = "head"
    override val children: MutableList<Tag> = mutableListOf()

    fun title(init: Title.() -> Unit): Tag = Title().apply { init() }.also { children.add(it) }
}

class Body : Tag {
    override val name: String
        get() = "body"
    override val children: MutableList<Tag> = mutableListOf()

    fun p(init: Paragraph.() -> Unit): Tag = Paragraph().apply { init() }.also { children.add(it) }
}

class Title : TagWithContent() {
    override val name: String
        get() = "title"
    override val children: MutableList<Tag> = mutableListOf()
}

class Paragraph : TagWithContent() {
    override val name: String
        get() = "p"
    override val children: MutableList<Tag> = mutableListOf()
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
