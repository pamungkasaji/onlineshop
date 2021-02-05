import { shippingState } from '../actions/shippingActions'

export const shippingReducer = (state = shippingState, action) => {
  switch (action.type) {
    case 'CITY_ALL':
      return { ...state, city: [...action.payload] }
    case 'PROV_ALL':
      return { ...state, prov: [...action.payload] }
    case 'CITY_IN_PROV':
      return { ...state, cityInProv: [...action.payload] }
    case 'SHIPPING_PRICE':
      return { ...state, minCost: action.payload}
    case 'SHIPPING_SUCCESS':
      return {
        ...state,
        cost: [...action.payload.cost],
        btnText: action.payload.btnText,
        btnDisabled: action.payload.btnDisabled
      }
    case 'SHIPPING_CLEANUP': {
      return {
        ...state,
        cityInProv: action.payload.cityInProv,
        btnText: action.payload.btnText,
        btnDisabled: action.payload.btnDisabled
      }
    }
    default:
      return state
  }
}
