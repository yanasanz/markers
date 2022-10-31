package com.example.maptest

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.maptest.databinding.PlacemarkCardBinding
import com.example.maptest.dto.Placemark

interface OnInteractionListener {
    fun onEdit(placemark: Placemark)
    fun onRemove(placemark: Placemark)
    fun onShowLocation(placemark: Placemark)
}

class PlacemarkAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Placemark, PlacemarkAdapter.PlacemarkCardViewHolder>(PlacemarkCardViewHolder.PlacemarkDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacemarkCardViewHolder {
        val cardBinding =
            PlacemarkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlacemarkCardViewHolder(
            cardBinding,
            onInteractionListener
        )
    }

    override fun onBindViewHolder(holder: PlacemarkCardViewHolder, position: Int) {
        val placemark = getItem(position)
        holder.bind(placemark)
    }

    class PlacemarkCardViewHolder(
        private val placemarkCardBinding: PlacemarkCardBinding,
        private val onInteractionListener: OnInteractionListener
    ) : RecyclerView.ViewHolder(placemarkCardBinding.root) {

        fun bind(placemark: Placemark) {
            placemarkCardBinding.apply {
                name.text = placemark.name
                description.text = placemark.description

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.placemark_card_option)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.edit -> {
                                    onInteractionListener.onEdit(placemark)
                                    true
                                }
                                R.id.remove -> {
                                    onInteractionListener.onRemove(placemark)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }

                placemarkCard.setOnClickListener{
                    onInteractionListener.onShowLocation(placemark)
                }
            }
        }

        class PlacemarkDiffCallback : DiffUtil.ItemCallback<Placemark>() {
            override fun areItemsTheSame(oldItem: Placemark, newItem: Placemark): Boolean {
                if (oldItem::class != newItem::class) {
                    return false
                }
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Placemark, newItem: Placemark): Boolean {
                return oldItem == newItem
            }
        }
    }
}
