def configure_webee(url, user, pass)
  WeBee::Api.user = user
  WeBee::Api.password = pass
  WeBee::Api.url = url
end