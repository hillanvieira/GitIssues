package br.com.hillan.gitissues.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.hillan.gitissues.R
import br.com.hillan.gitissues.databinding.IssueItemBinding
import br.com.hillan.gitissues.models.Issue
import java.text.Format
import java.text.SimpleDateFormat

class IssueListAdapter(
    private val issues: List<Issue>,
    private val context: Context,
    var whenClicked: (issue: Issue) -> Unit = {}

) : RecyclerView.Adapter<IssueListAdapter.ViewHolder>() {

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

    inner class ViewHolder(private val binding: IssueItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var issue: Issue

        init {
            itemView.setOnClickListener {
                if (::issue.isInitialized) {
                    whenClicked(issue)
                }
            }
        }

        fun bindView(issue: Issue) {
            this.issue = issue
//            val title: TextView = itemView.findViewById(R.id.issue_item_title) use without biding
//            val state: TextView = itemView.findViewById(R.id.issue_item_state) use without biding

            val title: TextView = binding.issueItemTitle
            val state: TextView = binding.issueItemState
            title.text = issue.title

            val f: Format = SimpleDateFormat("dd/MM/yy")
            val strDate: String = f.format(issue.createdAt)
            state.text = "${issue.state} $strDate"
        }

    }
}

