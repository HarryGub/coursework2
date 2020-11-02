import datetime
from django.db import models


class Task(models.Model):
    number = models.IntegerField(editable=True, default=0, unique=True, primary_key=True)
    category = models.CharField(default="all", max_length=15)
    correct_answers = models.TextField(default="")
    question = models.TextField(default="text")
    material = models.FileField(blank=True)
    # 0 - text
    # 1 - image
    type = models.IntegerField(default=0)
    hint = models.TextField()
    hint_price = models.IntegerField(default=5)
    points = models.IntegerField(default=10)


class Team(models.Model):
    name = models.CharField(default="player", max_length=30, unique=True)
    key = models.CharField(default="teamkey", max_length=8, unique=True)
    password = models.CharField(default="", max_length=8, unique=True)
    points = models.IntegerField(default=0)
    captain = models.CharField(default="vk.com/", max_length=30, unique=True)
    participants = models.IntegerField(default=1)
    time_of_begin = models.TimeField(default='10:00')
