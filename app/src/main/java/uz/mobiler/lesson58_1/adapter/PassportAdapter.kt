package uz.mobiler.lesson58_1.adapter

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import uz.mobiler.lesson58_1.R
import uz.mobiler.lesson58_1.database.AppDatabase
import uz.mobiler.lesson58_1.database.entity.Passport
import uz.mobiler.lesson58_1.databinding.PasportItemBinding

class PassportAdapter(
    val c: Context,
    val lottie: LottieAnimationView,
    var passportList: ArrayList<Passport>,
    val listener: OnItemClickListener
) :
    RecyclerView.Adapter<PassportAdapter.Vh>() {
    val appDatabase: AppDatabase by lazy {

        AppDatabase.getInstance(c)
    }

    inner class Vh(val pasportItemBinding: PasportItemBinding) :
        RecyclerView.ViewHolder(pasportItemBinding.root) {

        fun onBind(passport: Passport, position: Int) {
            pasportItemBinding.apply {
                count.text = position.plus(1).toString()
                name.text =
                    passport.lastName + " " + passport.firstName
                series.text =
                    passport.seriesLetter + " " + passport.seriesNumber
                itemView.setOnClickListener {
                    listener.onItemClick(
                        passport,
                        position
                    )
                }
                menu.setOnClickListener {
                    popupMenus(it, passport, position)
                }
            }
        }

        private fun popupMenus(v: View?, passport: Passport, position: Int) {
            val popupMenus = PopupMenu(c, v)
            popupMenus.inflate(R.menu.popup_menu)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> {
                        val bundle = Bundle()
                        bundle.putInt("id", passport.id)
                        Navigation.findNavController(pasportItemBinding.root)
                            .navigate(R.id.editFragment, bundle)
                        true
                    }
                    R.id.delete -> {
                        AlertDialog.Builder(c).setCancelable(false)
                            .setMessage("Haqiqatda o'chirmoqchimisiz?")
                            .setPositiveButton("Ha") { dialog, _ ->
                                appDatabase.passportDao().deletePassport(passport)
                                passportList.remove(passport)
                                notifyDataSetChanged()
                                if (passportList.isNotEmpty()) {
                                    lottie.visibility = View.INVISIBLE
                                } else {
                                    lottie.visibility = View.VISIBLE
                                }
                                dialog.dismiss()
                            }
                            .setNegativeButton("Yo’q") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(PasportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(passportList[position], position)
    }

    override fun getItemCount(): Int = passportList.size

    interface OnItemClickListener {
        fun onItemClick(passport: Passport, position: Int)
    }

    fun filterList(filteredList: ArrayList<Passport>) {
        passportList = filteredList
        notifyDataSetChanged()
    }
}