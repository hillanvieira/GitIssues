package br.com.hillan.gitissues.adapter

import java.text.Format
import android.view.ViewGroup
import android.widget.TextView
import android.content.Context
import java.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.cardview.widget.CardView
import br.com.hillan.dataissues.data.Issue
import androidx.recyclerview.widget.RecyclerView
import br.com.hillan.gitissues.databinding.IssueItemBinding

class IssueListAdapter(
    private val issues: List<Issue>,
    private val context: Context,
    var whenClicked: (issue: Issue) -> Unit = {}

) : RecyclerView.Adapter<IssueListAdapter.ViewHolder>() {

    var cardExpanded: CardView = CardView(context)
    lateinit var cardExpandedSelected: CardView
    var hasCardExpanded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            IssueItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

//        val view = LayoutInflater.from(context).inflate(R.layout.issue_item, parent, false) use without biding
//        return ViewHolder(view)                                                             use without biding
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

    inner class ViewHolder(private val binding: IssueItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var issue: Issue

        val card: CardView = binding.issueCard
        val cardSelected: CardView = binding.issueCardSelected

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

