package uz.mobiler.lesson58_1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import uz.mobiler.lesson58_1.R
import uz.mobiler.lesson58_1.database.entity.Region
import uz.mobiler.lesson58_1.databinding.RegionItemBinding

class RegionAdapter(context: Context, val regionList: List<Region>) :
    ArrayAdapter<Region>(context, R.layout.region_item) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val regionItemBinding: RegionItemBinding
        if (convertView == null) {
            regionItemBinding =
                RegionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            regionItemBinding = RegionItemBinding.bind(convertView)
        }
        regionItemBinding.regionNameTv.text = regionList[position].name
        return regionItemBinding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val regionItemBinding: RegionItemBinding
        if (convertView == null) {
            regionItemBinding =
                RegionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            regionItemBinding = RegionItemBinding.bind(convertView)
        }
        regionItemBinding.regionNameTv.text = regionList[position].name
        return regionItemBinding.root
    }

    override fun getCount(): Int {
        return regionList.size
    }
}