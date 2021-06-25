package br.com.hillan.gitissues.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.hillan.gitissues.IssueViewModel
import br.com.hillan.gitissues.adapter.IssueListAdapter
import br.com.hillan.gitissues.databinding.FragmentIssueListBinding
import br.com.hillan.gitissues.models.Issue
import org.koin.android.viewmodel.ext.android.viewModel

class IssueListFragment() : Fragment() {

    //Lazy Inject ViewModel Koin
    private val mIssueViewModel: IssueViewModel by viewModel()
    private lateinit var binding: FragmentIssueListBinding

    // View initialization logic
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIssueListBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Post view initialization logic
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mIssueViewModel.allIssues.observe(viewLifecycleOwner, {
            if (it != null) {
                configureRecyclerView(IssueListAdapter(it, requireActivity()))
            }
        })

    }

    private fun configureRecyclerView(adapter: IssueListAdapter) {

        val recyclerView: RecyclerView = binding.issueList
        recyclerView.adapter = adapter
        adapter.whenClicked = this::openIssueViewFragment
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        //val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
    }

    private fun openIssueViewFragment(it: Issue) {

        val directions =
            IssueListFragmentDirections.showDetails(it.id!!)
        Log.i("ISSUE_ID", "${it.id}")
        findNavController().navigate(directions)
    }

}