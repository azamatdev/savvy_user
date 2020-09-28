package uz.mymax.savvyenglish.ui.explanation

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import uz.mymax.savvyenglish.model.LessonData
import uz.mymax.savvyenglish.model.LessonRule
import uz.mymax.savvyenglish.databinding.ItemLessonRuleBinding
import uz.mymax.savvyenglish.network.response.ExplanationResponse

class ExplanationAdapter : RecyclerView.Adapter<ExplanationAdapter.LessonVH>() {

    private val list = LessonData.getLessonList()
    private var explanations: List<ExplanationResponse>? = null
    var temp: String =
        "<p>\uD83D\uDD38Dunyodagi deyarli barcha tillarda zamon kategoriyasi mavjud. <br />\uD83D\uDD38Ingliz tilida zamon tushunchasi &ldquo;tense&rdquo; atamasi bilan ifodalanadi. <br />\uD83D\uDD38Macmillian English Dictionary for Advanced Learners lug'atida zamon quyidagicha izohlangan:</p>\n" +
                "<p>▪️tense &ndash; a~ form of verb used for showing when something happens. For example, &lsquo;I goʻ is the present tense and &lsquo;I went&rsquo; is the past tense. <br />ℹ️Ya'ni, zamon- bu biror ish-harakatning qachon sodir boʻlganligini koʻrsatadigan fe&rsquo;l shaklidir, Masalan &ldquo;Men boraman&rdquo; - bu hozirgi zamon, &ldquo;Men bordim&rdquo; esa oʻtgan zamondir. <br />\uD83D\uDCA1Demak, zamon ish-harakatning qachon bajaril(ayot)ganligi nazarda tutadi.</p>\n" +
                "<p>▶️Xulosa: Ish-harakat qachon bajarilayotganligi yoki bajarilganligiga qarab oʻzimizga kerak boʻlgan grammatik qoliplarni (zamonlar shakli) tanlaymiz.</p>"

    fun updateList(newExplanations: List<ExplanationResponse>) {
        this.explanations = newExplanations
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonVH {
        val view =
            ItemLessonRuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonVH(view.root)

    }

    override fun getItemCount() = explanations?.size ?: 0

    override fun onBindViewHolder(holder: LessonVH, position: Int) {
        holder.binding!!.txtLessonNumber.text = (holder.adapterPosition + 1).toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.binding!!.txtRule.text =
                Html.fromHtml(explanations?.get(position)!!.text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            holder.binding!!.txtRule.text = Html.fromHtml(explanations?.get(position)!!.text)
        }

    }

    class LessonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = DataBindingUtil.getBinding<ItemLessonRuleBinding>(itemView)
    }

}