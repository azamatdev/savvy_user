package uz.mymax.savvyenglish.model

class Topic(
    var lessonNumber: Int = 0,
    var topicTitle: String = "",
    var isLocked: Boolean = false
) : TopicType {
    override fun type() = 0

}

class TopicHeader(
    var title: String
) : TopicType {
    override fun type() = 1
}

interface TopicType {
    fun type(): Int
}

class MockData {
    companion object {
        var topics = listOf<String>(
            "Ingliz tilida zamon tushunchasi",
            "Present Continuous tense",
            "Present Continuous zamonining qoʻllanish holatlari",
            "Present Simple tense",
            "Present Simple Tense ning qoʻllanish holatlari",
            "Present Simple Tense qo'llanishining maxsus holatlari",
            "Present Continuous va Present Simple Tense qo'llanishidagi o'ziga xosliklar",
            "Ingliz tilida eng keng tarqalgan holat fe'llari",
            "Ba'zi fe'llarning ham oddiy ham davomiy zamonlarda qo'llanishi",
            "Ingliz tilida fe'l shakllari",
            "Modal fe'llarning umumiy qoidalari",
            "can/could modal fe'llari ishlatilishi",
            "O'tgan zamonda could va was/were able to+infinitiv qurilmalarining farqi",
            "could modal fe'lining kelajak uchun va boshqa ma'nolarda ishlatilishi",
            "May va might modal fe'llarining hozirgi zamon uchun ishlatilishi",
            "if va wish qatnashgan gaplar (kirish darsi)",
            "First Conditional (Birinchi toifadagi shart ergash gaplar)",
            "Second Conditional (Ikkinchi toifadagi shart ergash gaplar)",
            "Third Conditional (Uchinchi toifadagi shart ergash gaplar)",
            "Zero Conditional (Nolinchi toifadagi shart ergash gaplar)",
            "Introduction to Active and passive voice",
            "Majhul nisbatning o'ziga xosliklari",
            "Majhul nisbatning zamonlarda qo'llanishi",
            "Modal fe'llarning majhul nisbatda qo'llanishi",
            "Majhul nisbat qurilmasi bilan axborot berish"
        )

        fun getTopicsList(): ArrayList<TopicType> {
            var list = ArrayList<TopicType>()

            for (i in 0 until 25) {
                when (i) {
                    0 -> list.add(TopicHeader("Zamonlar"))
                    10 -> list.add(TopicHeader("Modal"))
                    15 -> list.add(TopicHeader("Conditionals"))
                    20 -> list.add(TopicHeader("Passive Voice"))
                }

                val topic = Topic(
                    lessonNumber = i + 1,
                    topicTitle = topics[i]
                )
                if (i < 4)
                    topic.isLocked = true
                list.add(topic)
            }

            return list;
        }
    }
}