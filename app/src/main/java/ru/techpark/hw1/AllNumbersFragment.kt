package ru.techpark.hw1

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AllNumbersFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = if (savedInstanceState == null) {
            (1..initialListSize).toList()
        } else {
            (1..savedInstanceState.getInt(LIST_KEY)).toList()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_numbers, container, false)
        val numbersAdapter = DataAdapter { number -> numberClicked(number) }
        numbersAdapter.data = list

        spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LAND_SPAN_NUMBER
        } else {
            PORTRAIT_SPAN_NUMBER
        }

        view.findViewById<RecyclerView>(R.id.numbers_recycler).apply {
            layoutManager = GridLayoutManager(context, spanCount)
            adapter = numbersAdapter
        }

        view.findViewById<Button>(R.id.add_number_button).setOnClickListener {
            list = (1..numbersAdapter.itemCount + 1).toList()
            numbersAdapter.data = list
            numbersAdapter.notifyDataSetChanged()
        }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(LIST_KEY, list.size)
        super.onSaveInstanceState(outState)
    }

    private fun numberClicked(number: String) {
        val fragmentManager = fragmentManager
        val numberFragment = OneNumberFragment()
        val bundle = Bundle()
        bundle.putString(KEY_NUMBER, number)
        numberFragment.arguments = bundle
        fragmentManager?.beginTransaction()?.replace(R.id.container, numberFragment)?.addToBackStack(null)?.commit()
    }

    companion object {
        private const val LIST_KEY: String = "listKey"
        private const val PORTRAIT_SPAN_NUMBER: Int = 3
        private const val LAND_SPAN_NUMBER: Int = 4
        private const val KEY_NUMBER: String = "oneNumber"
        private const val initialListSize: Int = 100
        private lateinit var list: List<Int>
        private var spanCount: Int = 0
        fun getNumbersKey(): String = KEY_NUMBER
    }
}