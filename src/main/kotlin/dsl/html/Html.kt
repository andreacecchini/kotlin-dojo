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

    fun head(init: Head.() -> Unit): Tag {
        val head = Head()
        head.init()
        children.add(head)
        return head
    }

    fun body(init: Body.() -> Unit): Tag {
        val body = Body()
        body.init()
        children.add(body)
        return body
    }
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

fun html(init: Html.() -> Unit): Tag {
    val html = Html()
    html.init()
    return html
}

fun main() {
    html {
        head {
        }
        body {
        }
    }.render().also(::println)
}
