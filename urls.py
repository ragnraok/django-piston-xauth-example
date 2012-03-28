from django.conf.urls.defaults import *
from piston.resource import Resource
from PistonTest.Name.modelsHandler import NameHandler, AuthHandler, RegisterHandler, LoginHandler
from PistonTest.piston.authentication import OAuthAuthentication

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

# resource for NameHandler
name_resource = Resource(handler=NameHandler, authentication=OAuthAuthentication())

# resource for RegisterHandler
register_resource = Resource(handler=RegisterHandler)

# resource for LoginHandler
login_resource = Resource(handler=LoginHandler)

urlpatterns = patterns('',
    # Example:
    # (r'^PistonTest/', include('PistonTest.foo.urls')),

    # Uncomment the admin/doc line below and add 'django.contrib.admindocs' 
    # to INSTALLED_APPS to enable admin documentation:
    # (r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    (r'^admin/', include(admin.site.urls)),

    (r'^name/$', name_resource),

    (r'^register/$', register_resource),

    (r'^login/$', login_resource),

)

# Add the urlpatterns for django-piston OAuth system
# django-oauth-plus style oauth
#urlpatterns += patterns('',
#    url(r'^oauth/', include('oauth_provider.urls')),
#)

# The following code is the piston-style oauth 
urlpatterns += patterns('PistonTest.piston.authentication',
    (r'^oauth/access_token/$','oauth_access_token'),
)

