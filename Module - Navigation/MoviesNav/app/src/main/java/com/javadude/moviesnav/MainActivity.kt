package com.javadude.moviesnav

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel : MovieViewModel
    private var actionMode : ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // NOTE: This view model instance is scoped to this activity.
        //       In the fragments, we need to also scope to the activity so they all share
        //       the same data
        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    fun startMovieDeleteMode() {
        if (actionMode == null) {
            startSupportActionMode(DeleteMovieActionMode())
        }
    }
    fun startActorDeleteMode() {
        if (actionMode == null) {
            startSupportActionMode(DeleteActorActionMode())
        }
    }
    fun invalidateActionMode() = actionMode?.invalidate()
    fun dismissActionMode() = actionMode?.finish()

    override fun onPause() {
        dismissActionMode() // just in case it's active
        super.onPause()
    }

    abstract inner class DeleteMode(val deleteAction : () -> Unit) : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            actionMode = mode
            mode.menuInflater.inflate(R.menu.menu_delete, menu)
            return true
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem) =
            when(item.itemId) {
                R.id.action_delete -> {
                    deleteAction()
                    mode.finish()
                    true
                }
                else -> false
            }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu) : Boolean {
            val numSelections = viewModel.movieSelectionManager.selections.value?.size ?: 0
            mode.title = "$numSelections selected"
            return true
        }
        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
        }
    }

    inner class DeleteMovieActionMode : DeleteMode( { viewModel.deleteSelectedMovies() } )
    inner class DeleteActorActionMode : DeleteMode( { viewModel.deleteSelectedActors() } )
}
