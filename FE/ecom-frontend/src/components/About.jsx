import { useState } from "react";
import ProductCart from "./shared/ProductCard";

const products = [
    {
        id: 1,
        name: "Product 1",
        image: "https://embarkx.com/sample/placeholder.png",
        description: "This is a sample product description.",
        price: 19.99,
        specialPrice: 14.99,
        about: true
    },
    {
        id: 2,
        name: "Product 2",
        image: "https://embarkx.com/sample/placeholder.png",
        description: "This is a sample product description.",
        price: 29.99,
        specialPrice: 24.99,
        about: true
    },
    {
        id: 3,
        name: "Product 3",
        image: "https://embarkx.com/sample/placeholder.png",
        description: "This is a sample product description.",
        price: 39.99,
        specialPrice: 34.99,
        about: true
    }
];

const About =  () => {

    const [count, setCount] = useState(0);

    return (
        <div className="max-w-7xl mx-auto px-4 py-8">
            <div className="text-slate-800 text-4xl font-bold text-center mb-12">
                About Us
            </div>

            <div className="flex flex-col lg:flex-row justify-between items-center mb-12">
                <div className="w-full md:w-1/2 text-center md:text-left">
                    <p className="text-lg mb-4">Welcome to our e-commerce store. We are a team of passionate individuals who love to create amazing products for our customers.
                        Our mission is to provide high-quality products at affordable prices, while also delivering exceptional customer service. We believe that shopping should be a fun and enjoyable experience, and we strive to make that a reality for our customers.
                    </p>
                </div>

                <div className="w-full md:w-1/2 mb-6 md:mb-0">
                    <img 
                        src="https://embarkx.com/sample/placeholder.png"
                        alt="About Us"
                        className="w-full h-auto rounded-lg shadow-lg transform-transition duration-300 hover:scale-105"
                    ></img>
                </div>

            </div>

            <div className="py-7 space-y-8">
                <h1 className="text-slate-800 text-4xl font-bold text-center">
                    Our Products
                </h1>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6" >
                    {products.map((product, index) => (
                        <ProductCart 
                            key={index}
                            image={product.image}
                            productName={product.name}
                            description={product.description}
                            specialPrice={product.specialPrice}
                            price={product.price}
                            about={product.about}
                        />

                    ))}

                </div>
            </div>
        </div>
    )
};

export default About;