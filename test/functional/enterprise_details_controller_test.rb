require 'test_helper'

class EnterpriseDetailsControllerTest < ActionController::TestCase
  setup do
    @enterprise_detail = enterprise_details(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:enterprise_details)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create enterprise_detail" do
    assert_difference('EnterpriseDetail.count') do
      post :create, :enterprise_detail => @enterprise_detail.attributes
    end

    assert_redirected_to enterprise_detail_path(assigns(:enterprise_detail))
  end

  test "should show enterprise_detail" do
    get :show, :id => @enterprise_detail.to_param
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @enterprise_detail.to_param
    assert_response :success
  end

  test "should update enterprise_detail" do
    put :update, :id => @enterprise_detail.to_param, :enterprise_detail => @enterprise_detail.attributes
    assert_redirected_to enterprise_detail_path(assigns(:enterprise_detail))
  end

  test "should destroy enterprise_detail" do
    assert_difference('EnterpriseDetail.count', -1) do
      delete :destroy, :id => @enterprise_detail.to_param
    end

    assert_redirected_to enterprise_details_path
  end
end
