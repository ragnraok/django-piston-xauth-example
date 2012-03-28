from piston.handler import BaseHandler
from PistonTest.Name.models import Name
from PistonTest.Name.models import ApiKey
from django.shortcuts import get_object_or_404
from django.http import HttpResponse
from django.contrib.auth.models import User
from django.contrib import auth
from PistonTest.Name.models import ApiKey, generate_unique_key


class NameHandler(BaseHandler):
        allowed_methods = ('GET', 'POST')
        fields = ('first_name', 'last_name', 'birth_date')
        model = Name

        # this is a get
        def read(self, request):
                return 'You GET'

        # this is a post
        def create(self, request):
                return 'You Post'

class AuthHandler(object):
        def is_authenticated(self, request):
                auth_string = request.META.get("HTTP_AUTHENTICATION", '')

                if not auth_string:
                        return False

                key = get_object_or_404(ApiKey, key=auth_string)

                if not key:
                        return False

                request.user = key.user_name

                return True

        def challenge(self):
                response = HttpResponse("Authorization Required")

                """
                It can set the Http header in HttpResponse object!
                """ 
                response['WWW-Authenticate'] = "Key Based Authentication"
                response.status_code = 401

                return response

class RegisterHandler(BaseHandler):
        allowed_method = ('POST')

        """
        POST, which used to create a user
        """
        def create(self, request):
                if request.raw_post_data:
                        user_info = request.raw_post_data.split("&")
                        name = user_info[0]
                        pwd = user_info[1]

                        user = User.objects.create_user(username=name,
                                                        password=pwd,
                                                        email='aa@bb.com')

                        unique_key = generate_unique_key()

                        api = ApiKey.objects.create(user_name=user, key=unique_key)

                        return "Register success, you user name is " + name  
                + ", you API key is " + api.key

class LoginHandler(BaseHandler):
        allowed_method = ('POST')

        """
        POST, which used to login
        """
        def create(self, request):
                if request.raw_post_data:
                        user_info = request.raw_post_data.split("&")
                        name = user_info[0]
                        pwd = user_info[1]

                        user = auth.authenticate(username=name, password=pwd)

                        if user is not None:
                                apiKey = get_object_or_404(ApiKey, user_name=user)
                                if apiKey:
                                        return apiKey.key
                                else:
                                        return 'authentication failed'
                        else:
                                return 'authentication failed'
