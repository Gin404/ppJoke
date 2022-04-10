package com.example.ppjoke.ui.publish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.libnavannotation.FragmentDestination
import com.example.ppjoke.databinding.FragmentPublishBinding

/**
 * @author: zangjin
 * @date: 2022/4/4
 */
@FragmentDestination(pageUrl = "main/tabs/publish", asStarter = false)
class PublishFragment: Fragment() {
    private var _binding: FragmentPublishBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(PublishViewModel::class.java)
        _binding = FragmentPublishBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.text.observe(viewLifecycleOwner) {
            binding.textPublish.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}