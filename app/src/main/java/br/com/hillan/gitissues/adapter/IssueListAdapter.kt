package br.com.hillan.gitissues.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import br.com.hillan.dataissues.data.Issue
import br.com.hillan.gitissues.databinding.IssueItemBinding
import java.text.Format
import java.text.SimpleDateFormat

class IssueListAdapter(
    private val issues: List<Issue>,
    private val context: Context,
    var whenClicked: (issue: Issue) -> Unit = {}

) : RecyclerView.Adapter<IssueListAdapter.ViewHolder>() {

    var cardExpanded: CardView = CardView(context)
    lateinit var cardExpandedSelected: CardView
    var hasCardExpanded = false

    private var _binding: IssueItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        _binding = IssueItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder()

//        val view = LayoutInflater.from(context).inflate(R.layout.issue_item, parent, false) use without biding
//        return ViewHolder(view)                                                             use without biding
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        _binding = null
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val issue = issues[position]
//        holder?.let {
//            it.title.text = issue.title
//            it.state.text = issue.state.toString()
//        }
        holder.bindView(issue)
    }

    override fun getItemCount(): Int {
        return issues.size
    }

    inner class ViewHolder:
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var issue: Issue

        private val card: CardView = binding.issueCard
        private val cardSelected: CardView = binding.issueCardSelected

        init {
            itemView.setOnClickListener {

                if (card != cardExpanded) {

                    if (::issue.isInitialized) {
                        whenClicked(issue)
                    }

                    cardSelected.visibility = VISIBLE
                    card.visibility = GONE

                    if (hasCardExpanded) {
                        cardExpandedSelected.visibility = GONE
                        cardExpanded.visibility = VISIBLE
                    }

                    cardExpandedSelected = cardSelected
                    cardExpanded = card

                    hasCardExpanded = true

                }
            }
        }

        fun bindView(issue: Issue) {
            this.issue = issue
//            val title: TextView = itemView.findViewById(R.id.issue_item_title) use without biding
//            val state: TextView = itemView.findViewById(R.id.issue_item_state) use without biding

            val title: TextView = binding.issueItemTitle
            val state: TextView = binding.issueItemState
            val stateSelected: TextView = binding.issueItemStateSelected
            val titleSelected: TextView = binding.issueItemTitleSelected

            title.text = issue.title
            titleSelected.text = issue.title

            val f: Format = SimpleDateFormat("dd/MM/yy")
            val strDate: String = f.format(issue.createdAt)
            state.text = "${issue.state} $strDate"
            stateSelected.text = "${issue.state} $strDate"
        }
    }
}

