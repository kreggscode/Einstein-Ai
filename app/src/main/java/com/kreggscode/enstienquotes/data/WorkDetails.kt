package com.kreggscode.enstienquotes.data

data class WorkDetail(
    val title: String,
    val description: String,
    val year: String,
    val type: String,
    val imageResId: Int? = null,
    val quoteCount: Int = 0
)

val famousWorks = listOf(
    WorkDetail(
        title = "Candide",
        description = "A satirical novella that follows the adventures of Candide, a young man taught by Pangloss that this is \"the best of all possible worlds.\"",
        year = "1759",
        type = "Novella",
        quoteCount = 0
    ),
    WorkDetail(
        title = "Zadig",
        description = "A philosophical tale about a young Babylonian named Zadig who experiences various adventures and misfortunes.",
        year = "1747",
        type = "Philosophical Tale",
        quoteCount = 0
    ),
    WorkDetail(
        title = "Letters Concerning the English Nation",
        description = "A series of essays comparing French and English culture, praising English tolerance and criticizing French absolutism.",
        year = "1733",
        type = "Essays",
        quoteCount = 0
    ),
    WorkDetail(
        title = "The Age of Louis XIV",
        description = "A historical work covering the reign of Louis XIV and the cultural developments of the period.",
        year = "1751",
        type = "History",
        quoteCount = 0
    ),
    WorkDetail(
        title = "Essay on the Customs and the Spirit of the Nations",
        description = "A comprehensive philosophical history of world civilization from ancient times to the 18th century.",
        year = "1756",
        type = "Philosophy",
        quoteCount = 0
    ),
    WorkDetail(
        title = "Treatise on Tolerance",
        description = "A passionate defense of religious tolerance written in response to the execution of Jean Calas.",
        year = "1763",
        type = "Treatise",
        quoteCount = 0
    ),
    WorkDetail(
        title = "Philosophical Dictionary",
        description = "A collection of articles on philosophical, theological, and political topics, often satirical in tone.",
        year = "1764",
        type = "Dictionary",
        quoteCount = 0
    ),
    WorkDetail(
        title = "Micromégas",
        description = "A science fiction story about a giant from Sirius who travels to Earth with a companion from Saturn.",
        year = "1752",
        type = "Science Fiction",
        quoteCount = 0
    )
)
