import React, { useState, useEffect } from 'react'
import axios from 'axios'
import { PayPalButton } from 'react-paypal-button-v2'
import { Link } from 'react-router-dom'
import { Button, Row, Col, ListGroup, Image, Card } from 'react-bootstrap'
import { LinkContainer } from 'react-router-bootstrap'
import { useDispatch, useSelector } from 'react-redux'
import Message from '../components/Message'
import Loader from '../components/Loader'
import { getOrderDetails, payOrder, midtransPay, deliverOrder } from '../actions/orderActions'
import queryString from 'query-string';
// import { ORDER_PAY_RESET } from '../constants/orderConstants'

const OrderPage = ({ match, history, location }) => {
  const orderId = match.params.id

  const queryParams = queryString.parse(location.search);
  console.log(queryParams);

  const [sdkReady, setSdkReady] = useState(false)

  const dispatch = useDispatch()

  const orderDetails = useSelector((state) => state.orderDetails)
  const { order, loading, error } = orderDetails

  const orderPay = useSelector((state) => state.orderPay)
  const { loading: loadingPay, success: successPay } = orderPay

  const orderDeliver = useSelector((state) => state.orderDeliver)
  const { loading: loadingDeliver, success: successDeliver } = orderDeliver

  const userLogin = useSelector((state) => state.userLogin)
  const { userInfo } = userLogin

  if (!loading) {
    //   Calculate prices
    const addDecimals = (num) => {
      return (Math.round(num * 100) / 100).toFixed(2)
    }

    // order.itemsPrice = addDecimals(
    //   order.orderItems.reduce((acc, item) => acc + item.price * item.qty, 0)
    // )
  }

  // no payment
  // useEffect(() => {
  //   dispatch(getOrderDetails(orderId))
  //   console.log('dispact getOrderDetails')
  // }, [dispatch, orderId])

  // payment baru
  useEffect(() => {
    if (!userInfo) {
      history.push('/login')
    }

    console.log("useeffect")

    const addPayPalScript = async () => {
      const { data: clientId } = await axios.get('/api/config/paypal')
      const script = document.createElement('script')
      script.type = 'text/javascript'
      script.src = `https://www.paypal.com/sdk/js?client-id=${clientId}`
      script.async = true
      script.onload = () => {
        setSdkReady(true)
      }
      document.body.appendChild(script)
    }

    if (!order || successPay) {
      dispatch({ type: 'ORDER_PAY_RESET' })
      dispatch(getOrderDetails(orderId))
    } else if (!order.paid) {
      if (!window.paypal) {
        addPayPalScript()
      } else {
        setSdkReady(true)
      }
    }
  }, [dispatch, orderId, successPay, order])

  // payment complete
  // useEffect(() => {
  //   if (!userInfo) {
  //     history.push('/login')
  //   }

  //   const addPayPalScript = async () => {
  //     const { data: clientId } = await axios.get('/api/config/paypal')
  //     const script = document.createElement('script')
  //     script.type = 'text/javascript'
  //     script.src = `https://www.paypal.com/sdk/js?client-id=${clientId}`
  //     script.async = true
  //     script.onload = () => {
  //       setSdkReady(true)
  //     }
  //     document.body.appendChild(script)
  //   }

  //   if (!order || successPay || successDeliver || order.orderId !== orderId) {
  //     dispatch({ type: 'ORDER_PAY_RESET' })
  //     dispatch({ type: 'ORDER_DELIVER_RESET' })
  //     dispatch(getOrderDetails(orderId))
  //   } else if (!order.paid) {
  //     if (!window.paypal) {
  //       addPayPalScript()
  //     } else {
  //       setSdkReady(true)
  //     }
  //   }
  // }, [dispatch, orderId, successPay, successDeliver, order])

  const paypalHandler = (paymentResult) => {
    console.log(paymentResult)
    dispatch(payOrder(orderId, {
      transaction_status: paymentResult.status,
      payment_method: "paypal"
    }))
  }

  const midtransHandler = () => {
    console.log(queryParams)
    dispatch(payOrder(queryParams.order_id, {
      transaction_status: queryParams.transaction_status,
      payment_method: "midtrans"
    }))
  }

  const deliverHandler = () => {
    dispatch(deliverOrder(order))
  }

  return loading ? (
    <Loader />
  ) : error ? (
    <Message variant='danger'>{error}</Message>
  ) : (
        <>
          <h1>Order {order.orderId}</h1>
          <Row>
            <Col md={8}>
              <ListGroup variant='flush'>
                <ListGroup.Item>
                  <h2>Shipping</h2>
                  {/* <p>
                    <strong>Name: </strong> {order.user.name}
                  </p>
                  <p>
                    <strong>Email: </strong>{' '}
                    <a href={`mailto:${order.user.email}`}>{order.user.email}</a>
                  </p> */}
                  <p>
                    <strong>Address:</strong>
                    {order.shippingAddress.address}, {order.shippingAddress.city}{' '}
                    {order.shippingAddress.postalCode},{' '}
                    {order.shippingAddress.country}
                  </p>
                  {order.delivered ? (
                    <Message variant='success'>
                      Delivered on {order.deliveredAt}
                    </Message>
                  ) : (
                      <Message variant='danger'>Not Delivered</Message>
                    )}
                </ListGroup.Item>

                <ListGroup.Item>
                  <h2>Payment Method</h2>
                  <p>
                    <strong>Method: </strong>
                    {order.paymentMethod}
                  </p>
                  {order.paid ? (
                    <Message variant='success'>Paid on {order.paidAt}</Message>
                  ) : (
                      <Message variant='danger'>Not Paid</Message>
                    )}
                </ListGroup.Item>

                <ListGroup.Item>
                  <h2>Order Items</h2>
                  {order.orderItems.length === 0 ? (
                    <Message>Order is empty</Message>
                  ) : (
                      <ListGroup variant='flush'>
                        {order.orderItems.map((item, index) => (
                          <ListGroup.Item key={index}>
                            <Row>
                              <Col md={1}>
                                <Image
                                  src={item.image}
                                  alt={item.name}
                                  fluid
                                  rounded
                                />
                              </Col>
                              <Col>
                                <Link to={`/product/${item.product}`}>
                                  {item.name}
                                </Link>
                              </Col>
                              <Col md={4}>
                                {item.qty} x ${item.price} = ${item.qty * item.price}
                              </Col>
                            </Row>
                          </ListGroup.Item>
                        ))}
                      </ListGroup>
                    )}
                </ListGroup.Item>
              </ListGroup>
            </Col>
            <Col md={4}>
              <Card>
                <ListGroup variant='flush'>
                  <ListGroup.Item>
                    <h2>Order Summary</h2>
                  </ListGroup.Item>
                  <ListGroup.Item>
                    <Row>
                      <Col>Items</Col>
                      <Col>${order.itemsPrice}</Col>
                    </Row>
                  </ListGroup.Item>
                  <ListGroup.Item>
                    <Row>
                      <Col>Shipping</Col>
                      <Col>${order.shippingPrice}</Col>
                    </Row>
                  </ListGroup.Item>
                  <ListGroup.Item>
                    <Row>
                      <Col>Total</Col>
                      <Col>${order.totalPrice}</Col>
                    </Row>
                  </ListGroup.Item>

                  {!order.paid && (
                    <ListGroup.Item>
                      {loadingPay && <Loader />}
                      {!sdkReady ? (
                        <Loader />
                      ) : (
                          <PayPalButton
                            amount={order.totalPrice}
                            onSuccess={paypalHandler}
                          />
                        )}
                    </ListGroup.Item>
                  )}

                  <ListGroup.Item>
                    <Button
                      type='button'
                      className='btn btn-block'
                      href={order.redirect_url}
                    >
                      Pembayaran
                    </Button>
                  </ListGroup.Item>

                  {loadingDeliver && <Loader />}
                  {userInfo.admin && order.paid && !order.delivered && (
                    <ListGroup.Item>
                      <Button
                        type='button'
                        className='btn btn-block'
                        onClick={deliverHandler}
                      >
                        Mark As Delivered
                      </Button>
                    </ListGroup.Item>
                  )}
                </ListGroup>
              </Card>
            </Col>
          </Row>
        </>
      )
}

export default OrderPage