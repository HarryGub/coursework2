from django.views.decorators.csrf import csrf_exempt

from .models import Task, Team

from string import ascii_uppercase, digits
import random

from django.http import JsonResponse
from django.core.exceptions import ObjectDoesNotExist
from django.http import FileResponse
import django.db.utils as utils
import json


@csrf_exempt
def login(request):
    ans = {'status': 'success'}
    data_json = json.loads(request.body)
    key = data_json['key']
    try:
        ans.update({'password': Team.objects.get(key=key).password})
    except ObjectDoesNotExist:
        ans.update({'status': 'failed'})
    return JsonResponse(ans)


@csrf_exempt
def get_points(request):
    ans = {'status': 'success'}
    data_json = json.loads(request.body)
    password = data_json['password']
    try:
        ans.update({'points': Team.objects.get(password=password).points})
    except ObjectDoesNotExist:
        ans.update({'status': 'failed'})
    return JsonResponse(ans)


@csrf_exempt
def get_tasks(request):
    ans = {'status': 'failed'}
    # ans.update(Task.objects.values('number', 'category', 'question', 'material', 'type'))
    for x in Task.objects.values('number', 'category', 'question', 'type'):
        ans.update({x['number']: x})
    ans.update({'status': 'success'})
    return JsonResponse(ans)


@csrf_exempt
def get_res(request):
    n = json.loads(request.body)['n']
    return FileResponse(Task.objects.get(number=n).material, 'rb')


@csrf_exempt
def send_answer(request):
    data_json = json.loads(request.body)
    task = Task.objects.get(number=data_json['number'])
    ans = {'status': 'failed'}
    if task.correct_answers != data_json['answer']:
        ans.update({'status': 'failed', 'reason': 'wrong'})
    else:
        ans.update({'status': 'success'})
        team = Team.objects.get(password=data_json['password'])
        team.points += 10
        team.save()
        ans.update()
    return JsonResponse(ans)


@csrf_exempt
def get_hint(request):
    ans = {'status': 'success'}
    data_json = json.loads(request.body)
    print(1)
    team = Team.objects.get(password=data_json['password'])
    print(1)
    if team.points >= 5:
        task = Task.objects.get(number=data_json['number'])
        ans.update({'hint': task.hint})
        team.points -= 5
        team.save()
        print(2)
    else:
        ans = {'status': 'failed'}
        print(3)
    return JsonResponse(ans)


@csrf_exempt
def get_results(request):
    return JsonResponse({'status': 'success', 'rating': Team.objects.order_by('-points')})
