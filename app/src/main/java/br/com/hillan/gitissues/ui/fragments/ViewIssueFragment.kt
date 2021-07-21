package br.com.hillan.gitissues.ui.fragments

import android.content.res.Configuration
import java.text.Format
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mukesh.MarkdownView
import com.bumptech.glide.Glide
import android.widget.ImageView
import br.com.hillan.gitissues.R
import java.text.SimpleDateFormat
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.com.hillan.dataissues.data.Issue
import br.com.hillan.gitissues.viewmodel.IssueViewModel
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import br.com.hillan.gitissues.databinding.FragmentViewIssueBinding
import br.com.hillan.gitissues.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewIssueFragment : Fragment() {

    private val issueViewModel: IssueViewModel by activityViewModels()
    private var _binding: FragmentViewIssueBinding? = null
    private val binding get() = _binding!!
    private val orientation:Int
        get() {
            return when(resources.configuration.orientation){
                Configuration.ORIENTATION_PORTRAIT -> Configuration.ORIENTATION_PORTRAIT
                Configuration.ORIENTATION_LANDSCAPE -> Configuration.ORIENTATION_LANDSCAPE
                Configuration.ORIENTATION_UNDEFINED -> Configuration.ORIENTATION_UNDEFINED
                else -> Configuration.ORIENTATION_PORTRAIT
            }
        }

    private lateinit var titleText: TextView
    private lateinit var dateText: TextView
    private lateinit var bodyText: MarkdownView
    private lateinit var imageView: ImageView
    private var issueIdByArgs: Long = 0

   // private val args: ViewIssueFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(orientation == Configuration.ORIENTATION_LANDSCAPE ){
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(activity is MainActivity){
            (activity as MainActivity?)?.title = "Issues"
        }
        _binding = FragmentViewIssueBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Post view initialization logic
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObservers() {
        issueViewModel.issueById.observe(viewLifecycleOwner, {
            if (it != null) {
                configureView(it)
            }
        })
    }

    private fun configureView(it: Issue) {

//        titleText = findViewById(R.id.view_issue_title) sem binding
//        bodyText  = findViewById(R.id.view_issue_body)  sem binding
//        imageView = findViewById(R.id.avatar_imageview) sem binding
//        dateText  = findViewById(R.id.date_textView)    sem binding

        titleText = binding.viewIssueTitle
        bodyText = binding.viewIssueBody
        imageView = binding.avatarImageview
        dateText = binding.dateTextView
        titleText.text = it.title

        val f: Format = SimpleDateFormat("dd/MM/yy")
        val strDate: String = f.format(it.createdAt)
        dateText.text = strDate

        bodyText.setMarkDownText(it.body)

        Glide.with(this).load(it.user.avatarUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .override(120, 120)
            .transform(CircleCrop())
            .into(imageView)

    }

}