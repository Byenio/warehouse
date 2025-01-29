"use server";

import { env } from "process";

export async function deleteProductAction(productId: string) {
    console.log(
        `${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/products/${productId}`
    );
    // const response = await fetch(`${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/products/${productId}`, {
    //     method: "DELETE",
    //     headers: {
    //         "Content-Type": "application/json",
    //     },
    // });

    // if (!response.ok) {
    //     throw new Error("Failed to delete product");
    // }
}
