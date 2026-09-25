package dsl.html

interface Tag {
    val name: String
    val children: MutableList<Tag>
    fun render(): String = "<$name>${children.joinToString("") { it.render() }}</$name>"
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
}

class Body : Tag {
    override val name: String
        get() = "body"
    override val children: MutableList<Tag> = mutableListOf()
}

fun html(init: Html.() -> Unit): Tag = Html().apply { init() }

fun main() {
    html {
        head {
        }
        body {
        }
    }.render().also(::println)
}
