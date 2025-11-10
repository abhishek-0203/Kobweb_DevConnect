package com.example.blogmultiplatform.navigation

import com.example.shared.Category
import com.example.blogmultiplatform.models.Constants.CATEGORY_PARAM
import com.example.blogmultiplatform.models.Constants.POST_ID_PARAM
import com.example.blogmultiplatform.models.Constants.QUERY_PARAM
import com.example.blogmultiplatform.models.Constants.UPDATED_PARAM

const val admin_signup_route = "/admin/signup"
const val admin_login_route = "/admin/login"
const val admin_home_route = "/admin/"
const val admin_my_post_route = "/admin/myposts"

sealed class Screen(val route: String) {
    object AdminHome : Screen(route = admin_home_route)
    object AdminLogin : Screen(route = admin_login_route)
    object AdminSignUp : Screen(route = admin_signup_route)
    object AdminCreate : Screen(route = "/admin/create") {
        fun passPostId(id: String) = "/admin/create?${POST_ID_PARAM}=$id"
    }

    object AdminMyPosts : Screen(route = admin_my_post_route) {
        fun searchByTitle(query: String) = "$admin_my_post_route?${QUERY_PARAM}=$query"
    }

    object AdminSuccess : Screen(route = "/admin/success") {
        fun postUpdated() = "/admin/success?${UPDATED_PARAM}=true"
    }

    object HomePage : Screen(route = "/")
    object SearchPage : Screen(route = "/search/query") {
        fun searchByCategory(category: Category) =
            "/search/query?${CATEGORY_PARAM}=${category.name}"

        fun searchByTitle(query: String) = "/search/query?${QUERY_PARAM}=$query"
    }

    object PostPage : Screen(route = "/posts/post") {
        fun getPost(id: String) = "/posts/post?${POST_ID_PARAM}=$id"
    }

    object ChatPage : Screen(route = "/chat")
    object ProfilePage : Screen(route = "/profile")

    object ContactPage : Screen(route = "/contact")

}
