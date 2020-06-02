package uz.mymax.savvyenglish.model

data class LessonRule(
    var lessonNumber: Int,
    var lessonRule: String

) : Lesson {
    override fun getType() : LessonType{
        return LessonType.RULE
    }
}

data class LessonQuestionTypeForm(
    var question: String,
    var choices: ArrayList<Answer>
) : Lesson {
    override fun getType() : LessonType {
        return LessonType.QUESTION
    }
}

data class LessonQuestionTypeSentence(
    var choices: ArrayList<Answer>
)

data class Answer(
    var choice: String,
    var isCorrect: Boolean
)

enum class QuestionType {
    SENTENCE_TYPE,
    CHOICE_FORM_TYPE
}
enum class LessonType {
    RULE,
    QUESTION
}

interface Lesson {
    fun getType() : LessonType
}

object LessonData {
    fun getLessonList(): ArrayList<Lesson> {
        var lessonList = ArrayList<Lesson>()
        var rule1 = LessonRule(
            lessonRule = "\uD83D\uDD38Dunyodagi deyarli barcha tillarda zamon kategoriyasi mavjud. \n" +
                    "\uD83D\uDD38Ingliz tilida zamon tushunchasi “tense” atamasi bilan ifodalanadi. \n" +
                    "\uD83D\uDD38Macmillian English Dictionary for Advanced Learners lug'atida zamon quyidagicha izohlangan: \n" +
                    "\n" +
                    "▪️tense – a form of verb used for showing when something happens. For example, ‘I goʻ is the present tense and ‘I went’ is the past tense. \n" +
                    "ℹ️Ya'ni, zamon- bu biror ish-harakatning qachon sodir boʻlganligini koʻrsatadigan fe’l shaklidir, Masalan “Men boraman” - bu hozirgi zamon, “Men bordim” esa oʻtgan zamondir.  \n" +
                    "\uD83D\uDCA1Demak, zamon ish-harakatning qachon bajaril(ayot)ganligi nazarda tutadi. \n" +
                    "\n" +
                    "▶️Xulosa: Ish-harakat qachon bajarilayotganligi yoki bajarilganligiga qarab oʻzimizga kerak boʻlgan grammatik qoliplarni (zamonlar shakli) tanlaymiz.",
            lessonNumber = 1
        )
        var rule2 = LessonRule(
            lessonRule = "\uD83D\uDCA1Ingliz tilida zamonlar 4 ta katta guruhga boʻlinib oʻrganiladi: \n" +
                    "1. Present Tenses: (Hozirgi zamonlar guruhi):\n" +
                    "\uD83D\uDD38Present Continuous Tense\n" +
                    "\uD83D\uDD38Present Simple Tense\n" +
                    "\uD83D\uDD38Present Perfect Tense\n" +
                    "\uD83D\uDD38Present Perfect Continuous Tense\n" +
                    "\n" +
                    "2. Past Tenses (Oʻtgan zamonlar guruhi):\n" +
                    "\uD83D\uDD38Past Continuous Tense\n" +
                    "\uD83D\uDD38Past Simple Tense\n" +
                    "\uD83D\uDD38Past Perfect Tense\n" +
                    "\uD83D\uDD38Past Perfect Continuous Tense\n" +
                    "\n" +
                    "3. Future Tenses (Kelasi zamonlar guruhi):\n" +
                    "\uD83D\uDD38Future Continuous Tense\n" +
                    "\uD83D\uDD38Future Simple Tense\n" +
                    "\uD83D\uDD38Future Perfect Tense\n" +
                    "\uD83D\uDD38Future Perfect Continuous Tense\n" +
                    "\n" +
                    "4. Future in the Past Tenses (Oʻtgan kelasi zamonlar guruhi)\n" +
                    "\uD83D\uDD38Future Continuous in the Past Tense\n" +
                    "\uD83D\uDD38Future Simple in the Past Tense\n" +
                    "\uD83D\uDD38Future Perfect in the Past Tense\n" +
                    "\uD83D\uDD38Future Perfect Continuous in the Past Tense\n" +
                    "\n" +
                    "\uD83D\uDCA1E’tibor bergan boʻlsangiz, har bir zamon guruhidagi zamonlarda harakatning qay tarzda bajarilishini ifodalashda umumiylik mavjud: \n",
            lessonNumber = 2
        )
        var rule3 = LessonRule(
            lessonRule = "▪️continuous – davomiy zamonlarda ish-harakatning davomiyligi ta'kidlanadi.\n" +
                    "\uD83D\uDD38I am working – Men ishlayapman. (Present continuous)\n" +
                    "\uD83D\uDD38I was working -  Men ishlayotgan edim. (Past continuous)\n" +
                    "\uD83D\uDD38I will be working - Men ishlayotgan bo'laman. (Future continuous)\n",
            lessonNumber = 3
        )

        lessonList.add(rule1)
        lessonList.add(rule2)
        lessonList.add(rule3)

        val answers1 = ArrayList<Answer>()
        answers1.add(
            Answer(
                choice = "am",
                isCorrect = true
            )
        )

        answers1.add(
            Answer(
                choice = "is",
                isCorrect = false
            )
        )
        val question1 = LessonQuestionTypeForm(
            question = "I _____  working",
            choices = answers1
        )
        lessonList.add(question1)

        val rule4 = LessonRule(
            lessonRule = "▪️simple- oddiy zamonlarda ish-harakatning sodir boʻlishi yoki boʻlganligi ta'kidlanadi.\n" +
                    "\uD83D\uDD38I work every day– Men har kuni ishlayman. (Present simple)\n" +
                    "\uD83D\uDD38I worked yesterday - Men kecha ishladim.(Past simple)\n" +
                    "\uD83D\uDD38I will work tomorrow - Men ertaga ishlayman. (Future simple)\n",
            lessonNumber = 4
        )
        lessonList.add(rule4);
        val answers2 = ArrayList<Answer>()
        answers2.add(
            Answer(
                choice = "did",
                isCorrect = false
            )
        )

        answers2.add(
            Answer(
                choice = "will",
                isCorrect = true
            )
        )
        val question2 = LessonQuestionTypeForm(
            question = "I _____  work tomorrow",
            choices = answers2
        )

        lessonList.add(question2)


        val rule5 = LessonRule(
            lessonRule = "▪️perfect  – tugallangan oddiy zamonlarda ish-harakatning tugallanganligi va natijasi mavjudligi ta'kidlanadi.\n" +
                    "\uD83D\uDD38I have finished the book – Men kitobni tugatdim. (Present perfect)\n" +
                    "\uD83D\uDD38I had finished the book - Men kitobni tugatgan edim.(Past perfect)\n" +
                    "\uD83D\uDD38I will have finished the book - Men kitobni tugatgan bo'laman. (Future perfect)\n",
            lessonNumber = 5
        )
        lessonList.add(rule5);

        val answers3 = ArrayList<Answer>()
        answers3.add(
            Answer(
                choice = "have",
                isCorrect = false
            )
        )

        answers3.add(
            Answer(
                choice = "has",
                isCorrect = true
            )
        )
        val question3 = LessonQuestionTypeForm(
            question = "I _____  finished the book",
            choices = answers3
        )

        lessonList.add(question3)

        val rule6 = LessonRule(
            lessonRule = "▪️perfect continuous – tugallangan davomiy zamonlarda ish-harakatning qancha muddat davom etganligi ta'kidlanadi.\n" +
                    "\uD83D\uDD38I have been working for four hours – Men toʻrt soatdan beri ishlayapman.(Present perfect continuous)\n" +
                    "\uD83D\uDD38I had been working for four hours – Men toʻrt soatdan beri ishlayotgan edim. (Past perfect continuous)\n" +
                    "\uD83D\uDD38I will have been working for four hours – Men toʻrt soatdan beri ishlayayotgan bo'laman.(Future perfect continuous)\n",
            lessonNumber = 6
        )

        lessonList.add(rule6)

        return  lessonList
    }
}
