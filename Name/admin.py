from django.contrib import admin
from PistonTest.Name.models import Name, ApiKey

class ApiKeyAdmin(admin.ModelAdmin):
        #fields = ('user_name', 'key')
        list_display = ('user_name', 'key')

admin.site.register(Name)
admin.site.register(ApiKey, ApiKeyAdmin)
