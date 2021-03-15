package uz.eldor.contactreadapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import net.cachapa.expandablelayout.ExpandableLayout

class ContactAdapter(context: Context) :
    ListAdapter<Contact, ContactAdapter.ContactVH>(ContactDiff()) {
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactVH {
        val view = inflater.inflate(R.layout.item_view, parent, false)
        return ContactVH(view)
    }

    override fun onBindViewHolder(holder: ContactVH, position: Int) =
        holder.onBind(getItem(position))

    class ContactVH(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        val tv_name: TextView? = itemView.findViewById(R.id.tv_name)
        val iv_image: ImageView? = itemView.findViewById(R.id.iv_image)
        val expand_item = itemView.findViewById<ExpandableLayout>(R.id.expand_layout)
        val tv_number:TextView = itemView.findViewById(R.id.tv_number)


        @SuppressLint("SetTextI18n")
        fun onBind(item: Contact) {
            tv_name?.text = item.name
            if (item.photo != null) {
                iv_image?.setImageURI(Uri.parse(item.photo))
            }

            containerView.setOnClickListener {
                expand_item.toggle()
                if (expand_item.isExpanded) {
                    if (item.numbers.isNotEmpty()) {
                        tv_number.text = " Number: ${item.numbers[0].number} "
                    }
                }
                if (expand_item.isExpanded) {
                    if (item.numbers.isNotEmpty()) {
                        tv_number.text = " Number: ${item.numbers[0].number} "
                    }
                }
            }

        }

    }


}

class ContactDiff : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact) = oldItem == newItem

}