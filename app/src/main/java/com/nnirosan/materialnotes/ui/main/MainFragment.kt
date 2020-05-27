package com.nnirosan.materialnotes.ui.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nnirosan.materialnotes.LOG_TAG
import com.nnirosan.materialnotes.R
import com.nnirosan.materialnotes.data.note.Note
import com.nnirosan.materialnotes.ui.shared.SharedViewModel
import com.nnirosan.materialnotes.utilities.SwipeToDeleteCallback
import org.koin.android.ext.android.get

class MainFragment : Fragment(), MainRecyclerAdapter.NoteClickListener, View.OnClickListener {
    val viewModel = get<SharedViewModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var navController: NavController
    private lateinit var adapter: MainRecyclerAdapter
    private var firstLaunchOccurred: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        navController = Navigation.findNavController(
            requireActivity(), R.id.nav_host
        )

        swipeLayout = view.findViewById(R.id.swipeLayout)
        swipeLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

        val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!
        val editIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit)!!
        
        viewModel.noteData.observe(viewLifecycleOwner, Observer {
            adapter = MainRecyclerAdapter(requireContext(), it as MutableList<Note>, this)
            recyclerView.adapter = adapter
            swipeLayout.isRefreshing = false
        })
        viewModel.activityTitle.observe(viewLifecycleOwner, Observer {
            requireActivity().title = it
        })

        val swipeHandler = object : SwipeToDeleteCallback(requireContext(), deleteIcon, editIcon) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.RIGHT) {
                    viewModel.deleteNoteRecord(adapter.selectItem(viewHolder))
                    adapter.removeItem(viewHolder)
                } else if (direction === ItemTouchHelper.LEFT) {
                    onNoteSelected(adapter.selectItem(viewHolder))
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        var fabNew = view.findViewById<FloatingActionButton>(R.id.createNoteButton)
        fabNew?.setOnClickListener(this)

        return view
    }

    override fun onNoteSelected(note: Note) {
        Log.i(LOG_TAG, "Selected note: ${note.title}")
        viewModel.selectedNote.value = note
        viewModel.myName.value = "bob"
        navController.navigate(R.id.action_nav_detail)
    }

    override fun onPause() {
        super.onPause()
        Log.i(LOG_TAG, "THIS ACTIVITY IS PAUSED")
        firstLaunchOccurred = true
    }

    override fun onResume() {
        super.onResume()
        Log.i(LOG_TAG, "THIS ACTIVITY IS RESUMED")
        if (firstLaunchOccurred) {
            Log.i(LOG_TAG, "THIS ACTIVITY IS RUNNING IF NOT FIRST LAUNCH")
            viewModel.refreshData()
        }
    }

    override fun onClick(v: View?) {
        Log.i(LOG_TAG, "SOMETHING WAS CLICKED")
        when (v?.id) {
            R.id.createNoteButton -> {
                Log.i(LOG_TAG, "FAB PRESSED")
                navController.navigate(R.id.action_nav_detail)
            }
            else -> {
                Log.i(LOG_TAG, "NOT MATCHING ANYTHING")
            }
        }
    }
}