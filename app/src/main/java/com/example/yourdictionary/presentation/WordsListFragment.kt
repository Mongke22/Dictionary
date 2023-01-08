package com.example.yourdictionary.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yourdictionary.R
import com.example.yourdictionary.databinding.FragmentWordsListBinding
import com.example.yourdictionary.presentation.recyclerview.adapters.WordListAdapter

class WordsListFragment : Fragment() {
    private var _binding: FragmentWordsListBinding? = null
    private val binding: FragmentWordsListBinding
        get() = _binding ?: throw RuntimeException("FragmentWordsListBinding == null")
    private lateinit var wordListAdapter: WordListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWordsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.wordList.observe(viewLifecycleOwner) {
            wordListAdapter.submitList(it)
            hideProgressDialog()
        }
    }

    private fun setupRecyclerView() {
        val rvWordList = binding.rvWordList
        with(rvWordList) {
            wordListAdapter = WordListAdapter()
            adapter = wordListAdapter
            recycledViewPool.setMaxRecycledViews(
                WordListAdapter.FROM_RUSSIAN,
                WordListAdapter.POOL_COUNT
            )
            recycledViewPool.setMaxRecycledViews(
                WordListAdapter.TO_RUSSIAN,
                WordListAdapter.POOL_COUNT
            )
        }
        setupListeners()
        setupSwipeListener(rvWordList)
    }

    private fun setupListeners(){
        setupClickListener()
    }
    private fun setupClickListener(){
        wordListAdapter.wordItemOnClickListener = { word ->
            val fragment =  WordDetailFragment.newInstance(viewModel, word)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.event_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupSwipeListener(view: View){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private lateinit var viewModel: MainViewModel
        private var mProgressDialog: Dialog? = null


        fun showCustomProgressDialog(context: Context) {
            mProgressDialog = Dialog(context)
            mProgressDialog!!.setContentView(R.layout.dialog_custom_progress)
            mProgressDialog!!.show()
        }

        fun hideProgressDialog() {
            if (mProgressDialog != null) {
                mProgressDialog!!.dismiss()
            }
        }

        fun newInstance(vm: MainViewModel): WordsListFragment {
            viewModel = vm
            return WordsListFragment().apply {
                arguments = Bundle().apply {
                    //putString(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }
        }
    }
}