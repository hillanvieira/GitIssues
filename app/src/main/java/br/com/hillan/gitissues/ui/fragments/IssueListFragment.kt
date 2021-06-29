package br.com.hillan.gitissues.ui.fragments

import android.content.Context
import android.content.res.Configuration
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
import br.com.hillan.gitissues.ui.MainActivity
import org.koin.android.viewmodel.ext.android.sharedViewModel

class IssueListFragment() : Fragment() {

    private val mIssueViewModel: IssueViewModel by sharedViewModel<IssueViewModel>()
    private lateinit var binding: FragmentIssueListBinding

    private val orientation:Int
        get() {
            return when(resources.configuration.orientation){
                Configuration.ORIENTATION_PORTRAIT -> Configuration.ORIENTATION_PORTRAIT
                Configuration.ORIENTATION_LANDSCAPE -> Configuration.ORIENTATION_LANDSCAPE
                Configuration.ORIENTATION_UNDEFINED -> Configuration.ORIENTATION_UNDEFINED
                else -> Configuration.ORIENTATION_PORTRAIT
            }
        }


    // View initialization logic
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(activity is MainActivity){
            (activity as MainActivity?)?.title = "Issues List"
        }
        binding = FragmentIssueListBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Post view initialization logic
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.i("INSTANCE", "$mIssueViewModel")
        mIssueViewModel.allIssues.observe(viewLifecycleOwner, {
            if (it != null) {
                configureRecyclerView(IssueListAdapter(it, requireActivity()))

                mIssueViewModel.idInput.value = it.last().id
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

        mIssueViewModel.idInput.value = it.id

        if(orientation == Configuration.ORIENTATION_PORTRAIT ){
            val directions =
            IssueListFragmentDirections.showDetails()
        findNavController().navigate(directions)

        }



//        val directions =
//            IssueListFragmentDirections.showDetails(it.id!!)
//        Log.i("ISSUE_ID", "${it.id}")
//        findNavController().navigate(directions)

    }



}