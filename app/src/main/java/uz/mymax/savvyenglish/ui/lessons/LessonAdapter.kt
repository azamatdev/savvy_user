package uz.mymax.savvyenglish.ui.lessons

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import uz.mymax.savvyenglish.model.LessonData
import uz.mymax.savvyenglish.model.LessonQuestionTypeForm
import uz.mymax.savvyenglish.model.LessonRule
import uz.mymax.savvyenglish.model.LessonType
import uz.mymax.savvyenglish.databinding.ItemLessonQuestionBinding
import uz.mymax.savvyenglish.databinding.ItemLessonRuleBinding

class LessonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = LessonData.getLessonList()
    var temp : String = "<p>\uD83D\uDD38Dunyodagi deyarli barcha tillarda zamon kategoriyasi mavjud. <br />\uD83D\uDD38Ingliz tilida zamon tushunchasi &ldquo;tense&rdquo; atamasi bilan ifodalanadi. <br />\uD83D\uDD38Macmillian English Dictionary for Advanced Learners lug'atida zamon quyidagicha izohlangan:</p>\n" +
            "<p>▪️tense &ndash; a form of verb used for showing when something happens. For example, &lsquo;I goʻ is the present tense and &lsquo;I went&rsquo; is the past tense. <br />ℹ️Ya'ni, zamon- bu biror ish-harakatning qachon sodir boʻlganligini koʻrsatadigan fe&rsquo;l shaklidir, Masalan &ldquo;Men boraman&rdquo; - bu hozirgi zamon, &ldquo;Men bordim&rdquo; esa oʻtgan zamondir. <br />\uD83D\uDCA1Demak, zamon ish-harakatning qachon bajaril(ayot)ganligi nazarda tutadi.</p>\n" +
            "<p>▶️Xulosa: Ish-harakat qachon bajarilayotganligi yoki bajarilganligiga qarab oʻzimizga kerak boʻlgan grammatik qoliplarni (zamonlar shakli) tanlaymiz.</p>"
    override fun getItemViewType(position: Int): Int {
        return when (list[position].getType()) {
            LessonType.RULE -> 0
            LessonType.QUESTION -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view =
                ItemLessonRuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LessonVH(view.root)
        } else {
            val view = ItemLessonQuestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            QuestionVH(view.root)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LessonVH -> {
                val rule = (list[position] as LessonRule)
                holder.binding!!.rule = rule
                holder.binding!!.txtRule.setText(Html.fromHtml(temp).toString())
            }
            is QuestionVH -> {
                val question = (list[position] as LessonQuestionTypeForm)
                holder.binding!!.questionTypeForm = question
            }
        }
    }

    class LessonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = DataBindingUtil.getBinding<ItemLessonRuleBinding>(itemView)
    }

    class QuestionVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = DataBindingUtil.getBinding<ItemLessonQuestionBinding>(itemView)
    }
}