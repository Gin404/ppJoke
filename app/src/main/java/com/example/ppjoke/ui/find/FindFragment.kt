package com.example.ppjoke.ui.find

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.didi.drouter.api.DRouter
import com.didi.librouterservice.IOrderService
import com.example.libnavannotation.FragmentDestination
import com.example.ppjoke.databinding.FragmentFindBinding

@FragmentDestination(pageUrl = "main/tabs/find", asStarter = false)
class FindFragment : Fragment() {

    private var _binding: FragmentFindBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(FindViewModel::class.java)

        _binding = FragmentFindBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textFind
        textView.setOnClickListener {
            DRouter.build(IOrderService::class.java)
                .setRemoteAuthority("com.didi.drouter.remote.RemoteProvider")
                .getService() //构造方法
                .sendOrder("hello")
        }
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}