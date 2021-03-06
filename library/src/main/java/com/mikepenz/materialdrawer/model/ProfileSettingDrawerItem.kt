package com.mikepenz.materialdrawer.model

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.ColorfulBadgeable
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.model.interfaces.Tagable
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable
import com.mikepenz.materialdrawer.util.DrawerUtils.setDrawerVerticalPadding
import com.mikepenz.materialdrawer.util.getPrimaryDrawerIconColor
import com.mikepenz.materialdrawer.util.getSelectableBackground

/**
 * Describes a [IProfile] being used with the [com.mikepenz.materialdrawer.widget.AccountHeaderView]
 */
open class ProfileSettingDrawerItem : AbstractDrawerItem<ProfileSettingDrawerItem, ProfileSettingDrawerItem.ViewHolder>(), IProfile, Tagable, Typefaceable, ColorfulBadgeable {
    override var icon: ImageHolder? = null
    override var name: StringHolder? = null
    override var description: StringHolder? = null

    var isIconTinted = false
    var descriptionTextColor: ColorStateList? = null

    override var badge: StringHolder? = null
    override var badgeStyle: BadgeStyle? = BadgeStyle()

    override var isSelectable = false

    override val type: Int
        get() = R.id.material_drawer_item_profile_setting

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_profile_setting

    @Deprecated("Please consider to replace with the actual property setter")
    fun withDescription(description: String): ProfileSettingDrawerItem {
        this.description = StringHolder(description)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withDescription(@StringRes descriptionRes: Int): ProfileSettingDrawerItem {
        this.description = StringHolder(descriptionRes)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withIconTinted(iconTinted: Boolean): ProfileSettingDrawerItem {
        this.isIconTinted = iconTinted
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        //get the context
        val ctx = holder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //set the item enabled if it is
        holder.itemView.isEnabled = isEnabled
        holder.name.isEnabled = isEnabled
        holder.description.isEnabled = isEnabled
        holder.icon.isEnabled = isEnabled

        //set the item selected if it is
        holder.itemView.isSelected = isSelected
        holder.name.isSelected = isSelected
        holder.description.isSelected = isSelected
        holder.icon.isSelected = isSelected

        //get the correct color for the background
        val selectedColor = this.selectedColor?.color(ctx) ?: getSelectedColor(ctx)
        //get the correct color for the text
        val color = this.textColor ?: getColor(ctx)
        val iconColor = this.iconColor ?: ctx.getPrimaryDrawerIconColor()
        val descriptionColor = this.descriptionTextColor ?: getColor(ctx)

        ViewCompat.setBackground(holder.view, ctx.getSelectableBackground(selectedColor, isSelectedBackgroundAnimated))

        StringHolder.applyTo(this.name, holder.name)
        holder.name.setTextColor(color)

        StringHolder.applyToOrHide(this.description, holder.description)
        holder.description.setTextColor(descriptionColor)

        if (typeface != null) {
            holder.name.typeface = typeface
            holder.description.typeface = typeface
        }

        //set the text for the badge or hide
        val badgeVisible = StringHolder.applyToOrHide(badge, holder.badge)
        //style the badge if it is visible
        if (badgeVisible) {
            badgeStyle?.style(holder.badge, getColor(ctx))
            holder.badgeContainer.visibility = View.VISIBLE
        } else {
            holder.badgeContainer.visibility = View.GONE
        }

        //define the typeface for our textViews
        if (typeface != null) {
            holder.badge.typeface = typeface
        }

        //set the correct icon
        ImageHolder.applyDecidedIconOrSetGone(icon, holder.icon, iconColor, isIconTinted, 2)

        //for android API 17 --> Padding not applied via xml
        setDrawerVerticalPadding(holder.view)

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    open class ViewHolder internal constructor(internal val view: View) : RecyclerView.ViewHolder(view) {
        internal val icon: ImageView = view.findViewById(R.id.material_drawer_icon)
        internal val name: TextView = view.findViewById(R.id.material_drawer_name)
        internal val description: TextView = view.findViewById(R.id.material_drawer_description)
        internal val badgeContainer: View = view.findViewById(R.id.material_drawer_badge_container)
        internal val badge: TextView = view.findViewById(R.id.material_drawer_badge)
    }
}
