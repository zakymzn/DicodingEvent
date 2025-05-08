package com.example.dicodingevent.data.local.entity

import androidx.room.*

@Entity(tableName = "favorite_event")
data class FavoriteEventEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "summary")
    var summary: String? = null,

    @ColumnInfo(name = "mediaCover")
    var mediaCover: String? = null,

    @ColumnInfo(name = "registrants")
    var registrants: Int? = null,

    @ColumnInfo(name = "imageLogo")
    var imageLogo: String? = null,

    @ColumnInfo(name = "link")
    var link: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "ownerName")
    var ownerName: String? = null,

    @ColumnInfo(name = "cityName")
    var cityName: String? = null,

    @ColumnInfo(name = "quota")
    var quota: Int? = null,

    @ColumnInfo(name = "beginTime")
    var beginTime: String? = null,

    @ColumnInfo(name = "endTime")
    var endTime: String? = null,

    @ColumnInfo(name = "category")
    var category: String? = null
)