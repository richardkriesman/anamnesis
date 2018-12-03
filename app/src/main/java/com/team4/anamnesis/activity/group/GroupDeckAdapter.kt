package com.team4.anamnesis.activity.group

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
import com.team4.anamnesis.db.entity.Deck

class GroupDeckAdapter(context: Context): RecyclerView.Adapter<GroupDeckAdapter.GroupDeckHolder>() {

    /**
     * A listener that fires when a deck is clicked.
     */
    var onDeckClicked: ((deck: Deck) -> Unit)? = null

    /**
     * A listener that fires when the user clicks the "delete" menu item.
     */
    var onDeckDeleteClicked: ((deck: Deck) -> Unit)? = null

    /**
     * A listener that fires when the user clicks the "edit" menu item.
     */
    var onDeckEditClicked: ((group: Deck) -> Unit)? = null

    private val c: Context = context // the kotlin compiler won't let us access context
    private var decks: List<Deck> = ArrayList() // list of flashcard decks

    override fun getItemCount(): Int {
        return decks.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupDeckHolder {
        val view: View = LayoutInflater.from(c).inflate(R.layout.item__deck, parent, false)
        return GroupDeckHolder(view)
    }

    override fun onBindViewHolder(holder: GroupDeckHolder, position: Int) {
        holder.bind(decks[position])
    }

    /**
     * Replace the set of decks.
     */
    fun setData(decks: List<Deck>) {
        this.decks = decks
        notifyDataSetChanged()
    }

    inner class GroupDeckHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var cardView: CardView = itemView.findViewById(R.id.item_deck_card)
        private var menuButton: ImageView = itemView.findViewById(R.id.item_deck_menu)
        private var textView: TextView = itemView.findViewById(R.id.item_deck_text)

        fun bind(deck: Deck) {
            textView.text = deck.name

            // listen for clicks on the card
            cardView.setOnClickListener {
                onDeckClicked?.invoke(deck)
            }

            // listen for clicks on the menu button
            menuButton.setOnClickListener {

                // create a popup menu
                val popupMenu = PopupMenu(c, menuButton)
                popupMenu.menuInflater.inflate(R.menu.item_deck, popupMenu.menu)

                // handle menu item clicks
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_deck_edit -> {
                            onDeckEditClicked?.invoke(deck)
                            true
                        }
                        R.id.menu_deck_delete -> { // delete the deck
                            onDeckDeleteClicked?.invoke(deck)
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