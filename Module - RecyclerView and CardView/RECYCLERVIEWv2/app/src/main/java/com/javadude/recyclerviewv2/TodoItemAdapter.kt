package com.javadude.recyclerviewv2

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.todo_item.*

class TodoItemAdapter(
        val lifecycleOwner: LifecycleOwner,
        val selectedItem : MutableLiveData<TodoItemEntity>,
        val items : LiveData<List<TodoItemEntity>>
) : androidx.recyclerview.widget.RecyclerView.Adapter<TodoItemAdapter.TodoItemViewHolder>() {

    init {
        items.observe(lifecycleOwner, Observer {
            notifyDataSetChanged()
        })
    }

    @UiThread
    override fun getItemCount(): Int {
        return items.value?.size ?: 0
    }

    @UiThread
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TodoItemViewHolder(view)
    }

    @UiThread
    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val item = items.value!![position]
        holder.bind(item)
    }

    inner class TodoItemViewHolder(override val containerView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(todoItemEntity: TodoItemEntity) {
            name.text = todoItemEntity.name
            priority.text = todoItemEntity.priority.toString()
            containerView.setOnClickListener {
                selectedItem.value = items.value!![adapterPosition]
            }
        }
    }
}