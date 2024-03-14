package com.mita.gamebuddymobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mita.gamebuddymobile.api.UserDataClass

class RandomUserAdapterClass(private val RandomUserList:ArrayList<RandomUserDataClass>)
    : RecyclerView.Adapter<RandomUserAdapterClass.ViewHolderClass>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.random_user_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val RandomUser = RandomUserList[position]
        holder.name.text = RandomUser.Randomname
        holder.gender.text = RandomUser.Randomgender
        holder.age.text = RandomUser.Randomage.toString()
    }
    override fun getItemCount(): Int {
        return RandomUserList.size
    }
    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.Random_Name)
        val gender : TextView = itemView.findViewById(R.id.Random_gender)
        val age : TextView = itemView.findViewById(R.id.Random_age)

    }
}