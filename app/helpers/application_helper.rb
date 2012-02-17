module ApplicationHelper
  
  # Define m to translate markdown
  def m(string)
    RDiscount.new(string).to_html.html_safe
  end

  # Define q to educate quotes
  def q(string)
    RubyPants.new(string).to_html.html_safe
  end

  def filter_quotes(string)
    string.gsub("\342\200\230", "'").gsub("\342\200\231", "'").gsub("\342\200\234", '"').gsub("\342\200\235", '"')
  end

end
