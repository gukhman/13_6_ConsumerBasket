package com.example.a13_6_consumerbasket

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class GoodsAdapter(context: Context, private val data: MutableList<Good>) :
    BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(pos: Int): Any? {
        return data[pos]
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.good_item, parent, false)
            holder = ViewHolder()
            holder.nameTV = view.findViewById(R.id.nameET)
            holder.weightTV = view.findViewById(R.id.weightET)
            holder.priceTV = view.findViewById(R.id.priceET)

            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val listItem = getItem(position) as Good
        holder.nameTV.text = listItem.name
        holder.weightTV.text = buildString {
            append(listItem.weight)
            append(" гр.")
        }
        holder.priceTV.text = buildString {
            append(listItem.price)
            append(" руб.")
        }

        return view
    }

    private class ViewHolder {
        lateinit var nameTV: TextView
        lateinit var weightTV: TextView
        lateinit var priceTV: TextView
    }
}