from django.contrib import admin
from django.urls import path
from polls import views

urlpatterns = [
    path('admin/', admin.site.urls),
    path(r'api/login', views.login, name='login'),
    path(r'api/get_tasks', views.get_tasks, name='get_tasks'),
    path(r'api/send_answer', views.send_answer, name='send_answer'),
    path(r'api/get_hint', views.get_hint, name='get_hint'),
    path(r'api/get_results', views.get_results, name='get_results'),
    path(r'api/get_res', views.get_res, name='get_img'),
    path(r'api/get_points', views.get_points, name='get_points'),
]
