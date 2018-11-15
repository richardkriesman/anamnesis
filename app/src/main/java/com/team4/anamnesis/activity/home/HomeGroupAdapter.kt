package com.team4.anamnesis.activity.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.team4.anamnesis.R
import com.team4.anamnesis.db.entity.FlashcardDeckGroup

class HomeGroupAdapter(context: Context): RecyclerView.Adapter<HomeGroupAdapter.HomeGroupHolder>() {

    /**
     * A listener that fires when a group card is clicked.
     */
    var onGroupClicked: ((group: FlashcardDeckGroup) -> Unit)? = null

    /**
     * A listener that fires when the user clicks the "delete" menu item.
     */
    var onGroupDeleteClicked: ((group: FlashcardDeckGroup) -> Unit)? = null

    /**
     * A listener that fires when the user clicks the "rename" menu item.
     */
    var onGroupRenameClicked: ((group: FlashcardDeckGroup) -> Unit)? = null

    private val c: Context = context // the kotlin compiler won't let us access context
    private var groups: List<FlashcardDeckGroup> = ArrayList() // list of flashcard groups

    override fun getItemCount(): Int {
        return groups.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGroupHolder {
        val view: View = LayoutInflater.from(c).inflate(R.layout.item_group, parent, false)
        return HomeGroupHolder(view)
    }

    override fun onBindViewHolder(holder: HomeGroupHolder, position: Int) {
        holder.bind(groups[position])
    }

    /**
     * Replace the set of groups.
     */
    fun setData(groups: List<FlashcardDeckGroup>) {
        this.groups = groups
        notifyDataSetChanged()
    }

    inner class HomeGroupHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var cardView: CardView = itemView.findViewById(R.id.item_group_card)
        private var menuButton: ImageView = itemView.findViewById(R.id.item_group_menu)
        private var textView: TextView = itemView.findViewById(R.id.item_group_text)

        fun bind(group: FlashcardDeckGroup) {
            textView.text = group.name

            // listen for clicks on the card
            cardView.setOnClickListener {
                onGroupClicked?.invoke(group)
            }

            // listen for clicks on the menu button
            menuButton.setOnClickListener {

                // create a popup menu
                val popupMenu = PopupMenu(c, menuButton)
                popupMenu.menuInflater.inflate(R.menu.item_group, popupMenu.menu)

                // handle menu item clicks
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_group_rename -> {
                            onGroupRenameClicked?.invoke(group)
                            true
                        }
                        R.id.menu_group_delete -> { // delete the group
                            onGroupDeleteClicked?.invoke(group)
                            true
                        }
                        else -> false
                    }
                }

                // show the popup menu
                popupMenu.show()
            }

        }

    }

}