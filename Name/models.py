from django.db import models
import datetime
from django.contrib.auth.models import User

# Create your models here.

class Name(models.Model):
        first_name = models.CharField(max_length=250)
        last_name = models.CharField(max_length=250)
        birth_date = models.DateTimeField(default=datetime.datetime.now)

def generate_unique_key():
        unique_key = User.objects.make_random_password(length=ApiKey.MAX_KEY_SIZE)

        while ApiKey.objects.filter(key=unique_key).count() > 0:
                unique_key = User.objects.make_random_password(length=ApiKey.MAX_KEY_SIZE)

        return unique_key

class ApiKey(models.Model):
        MAX_KEY_SIZE = 10

        user_name = models.ForeignKey(User, editable=False, unique=True)
        key = models.CharField(max_length=MAX_KEY_SIZE, editable=False, unique=True)

