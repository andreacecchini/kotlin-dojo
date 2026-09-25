package dsl.html

interface Tag {
    val name: String
    val children: MutableList<Tag>
    fun renderInner(): String = children.joinToString("") { it.render() }
    fun render(): String = "<$name>${renderInner()}</$name>"
}

interface TagWithContent : Tag {
    var content: String?
    override fun renderInner(): String = (content ?: "") + super.renderInner()
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

class Title : TagWithContent {
    override val name: String
        get() = "title"
    override val children: MutableList<Tag>
        get() = mutableListOf()
    override var content: String? = null

    operator fun String.unaryMinus() {
        content = this
    }
}

class Paragraph : TagWithContent {
    override val name: String
        get() = "p"
    override val children: MutableList<Tag>
        get() = mutableListOf()
    override var content: String? = null

    operator fun String.unaryMinus() {
        content = this
    }
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
