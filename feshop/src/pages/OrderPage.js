import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { Button, Row, Col, ListGroup, Image, Card } from 'react-bootstrap'
import { LinkContainer } from 'react-router-bootstrap'
import { useDispatch, useSelector } from 'react-redux'
import Message from '../components/Message'
import Loader from '../components/Loader'
import { getOrderDetails, payOrder, midtransPay, deliverOrder } from '../actions/orderActions'
import queryString from 'query-string';
import { rupiahFormat } from '../utils/rupiahFormat'
// import { ORDER_PAY_RESET } from '../constants/orderConstants'

const OrderPage = ({ match, history, location }) => {

  const queryParams = queryString.parse(location.search);
  console.log(queryParams);

  const orderId = match.params.id ? match.params.id : queryParams.order_id

  const dispatch = useDispatch()

  const orderDetails = useSelector((state) => state.orderDetails)
  const { order, loading, error } = orderDetails

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
  useEffect(() => {
    dispatch(getOrderDetails(orderId))
  }, [dispatch, orderId])

  // const midtransHandler = () => {
  //   console.log('query' + queryParams)
  //   dispatch(payOrder(queryParams.order_id, {
  //     transaction_status: queryParams.transaction_status,
  //     payment_method: "midtrans"
  //   }))
  // }

  const deliverHandler = () => {
    dispatch(deliverOrder(order))
  }

  return loading ? (
    <Loader />
  ) : error ? (
    <Message variant='danger'>{error}</Message>
  ) : (
        <>
          <h2>Informasi Pemesanan</h2>

          {queryParams.status_code &&
            (
              queryParams.status_code === '200' ? (
                <Message variant='success'>Pembayaran Berhasil</Message>
              ) : (
                  <Message variant='danger'>Pembayaran Gagal</Message>
                )
            )
          }

          <Row>
            <Col md={8}>
              <ListGroup variant='flush'>
                <ListGroup.Item>
                  <h4>Pengiriman</h4>
                </ListGroup.Item>
                {/* <p>
                    <strong>Name: </strong> {order.user.name}
                  </p>
                  <p>
                    <strong>Username: </strong>{' '}
                    <a href={`mailto:${order.user.username}`}>{order.user.username}</a>
                  </p> */}
                <ListGroup.Item>
                  <Row>
                    <Col md={3}> <strong>Address :</strong> </Col>
                    <Col md={9}> {order.shipping.address} {', '} {order.shipping.city}{', '}
                      {order.shipping.province} </Col>
                  </Row>

                  <Row>
                    <Col md={3}> <strong>No HP :</strong> </Col>
                    <Col md={9}> {order.shipping.phone} </Col>
                  </Row>
                </ListGroup.Item>
                <ListGroup.Item>
                  {order.delivered ? (
                    <Message variant='success'>
                      Delivered on {order.deliveredAt}
                    </Message>
                  ) : (
                      <Message variant='info'>Belum Diantar</Message>
                    )}
                </ListGroup.Item>

                <ListGroup.Item>
                  <h4>Pembayaran</h4>
                  {order.payment.paid ? (
                    <>
                      <p>
                        {order.payment.paymentType && (
                          <strong>Metode Pembayaran: {order.payment.paymentType} </strong>
                        )
                      }
                      </p>
                      <Message variant='success'>Sudah dibayar {order.paidAt}</Message>
                    </>
                  ) : (
                      <Message variant='info'>Belum Dibayar</Message>
                    )}
                </ListGroup.Item>

                <ListGroup.Item>
                  <h4>Daftar Produk</h4>
                  {order.orderItems.length === 0 ? (
                    <Message>Order is empty</Message>
                  ) : (
                      <ListGroup variant='flush'>
                        {order.orderItems.map((item, index) => (
                          <ListGroup.Item key={index}>
                            <Row>
                              <Col>
                                <Link to={`/product/${item.product}`}>
                                  {item.name}
                                </Link>
                              </Col>
                              <Col md={4}>
                                {item.qty} x {rupiahFormat(item.price)} = {rupiahFormat(item.qty * item.price)}
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
                    <h4>Rangkuman Pemesanan</h4>
                  </ListGroup.Item>
                  <ListGroup.Item>
                    <Row>
                      <Col>Total Harga Produk</Col>
                      <Col>{rupiahFormat(order.itemsPrice)}</Col>
                    </Row>
                  </ListGroup.Item>
                  <ListGroup.Item>
                    <Row>
                      <Col>Biaya Pengiriman</Col>
                      <Col>{rupiahFormat(order.shipping.shippingPrice)}</Col>
                    </Row>
                  </ListGroup.Item>
                  <ListGroup.Item>
                    <Row>
                      <Col>Total Pembayaran</Col>
                      <Col>{rupiahFormat(order.itemsPrice + order.shipping.shippingPrice)}</Col>
                    </Row>
                  </ListGroup.Item>

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