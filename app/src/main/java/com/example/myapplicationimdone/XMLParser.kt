package com.example.myapplicationimdone

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

data class Question(val title: String?, val summary: String?, val link: String?,val publishedDate: String?) {
    override fun toString(): String = title!!
}

class XMLParser {
    private val ns: String? = null

    fun parse(inputStream: InputStream): List<Question> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readQuestions(parser)
        }
    }

    private fun readQuestions(parser: XmlPullParser): List<Question> {
        val questions = mutableListOf<Question>()
        parser.require(XmlPullParser.START_TAG, ns, "feed")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "entry") {
                parser.require(XmlPullParser.START_TAG, ns, "entry")
                var title: String? = null
                var summary: String? = null
                var link: String? = null
                var published: String? = null
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    when (parser.name) {
                        "title" -> title = readTitle(parser)
                        "summary" -> summary = readSummary(parser)
                        "link" -> link = readLink(parser)
                        "published" -> published = readPublishedDate(parser)
                        else -> skip(parser)
                    }
                }
                questions.add(Question(title,summary,link,published))
            } else {
                skip(parser)
            }
        }
        return questions
    }

    private fun readPublishedDate(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "published")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "published")
        return title
    }

    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    private fun readLink(parser: XmlPullParser): String {
        var link = ""
        parser.require(XmlPullParser.START_TAG, ns, "link")
        val tag = parser.name
        val relType = parser.getAttributeValue(null, "rel")
        if (tag == "link") {
            if (relType == "alternate") {
                link = parser.getAttributeValue(null, "href")
                parser.nextTag()
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link")
        return link
    }

    private fun readSummary(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "summary")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "summary")
        return summary
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}