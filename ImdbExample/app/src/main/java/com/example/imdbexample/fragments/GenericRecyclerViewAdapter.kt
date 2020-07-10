package com.example.imdbexample.fragments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


abstract class GenericRecyclerViewAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var listItems: List<T>
    private var mListener: OnListFragmentInteractionListener? = null
    private val mOnClickListener: View.OnClickListener

    constructor(listItems: List<T>) {
        this.listItems = listItems
    }

    constructor(aListener: OnListFragmentInteractionListener?) {
        mListener = aListener
        listItems = emptyList()
    }

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag
            mListener?.onListFragmentInteraction(item)
        }
    }

    fun setItems(listItems: List<T>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    fun getItems(): List<T> {
        return this.listItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
            , viewType
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Binder<T>).bind(listItems[position])
        with(holder.itemView) {
            tag = listItems[position]
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position, listItems[position])
    }

    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder

    internal interface Binder<T> {
        fun bind(data: T)
    }
}
